(ns planted.auth
  (:require [cemerick.friend :as friend]
            [cemerick.friend.credentials :as creds]
            [cemerick.friend.workflows :as workflows]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [friend-oauth2.workflow :as oauth2]
            [friend-oauth2.util :refer [format-config-uri]]))

(defn- lookup-roles
  [db token]
  ;; TODO lookup roles from DB or some other appropriate source
  (if (.equals "admin" token)
    {:identity token :roles #{::user ::admin}}
    {:identity token :roles #{::user}}))

(defn- check-missing-google-config
  [variable name example]
  (if (nil? variable)
    (throw (Exception. (str "For google auth, must set '" name "' config value (" example ")")))))

(defn- build-client-config
  [config]
  (let [domain (:domain config)
        callback (get-in config [:oauth-callback] "/oauth2callback")
        {:keys [client-id client-secret] :as client-config} config]
    (check-missing-google-config domain "domain" "like https://mysite.com")
    (check-missing-google-config client-id "client-id" "get from google dev console")
    (check-missing-google-config client-secret "client-secret" "get from google dev console")
    (cons client-config {:callback {:domain domain :path callback}})))

(defn- build-uri-config
  [client-config]
  {:authentication-uri {:url "https://accounts.google.com/o/oauth2/auth"
                        :query {:client_id (:client-id client-config)
                                :response_type "code"
                                :redirect_uri (format-config-uri client-config)
                                :scope "email"}}
   :access-token-uri {:url "https://accounts.google.com/o/oauth2/token"
                      :query {:client_id (:client-id client-config)
                              :client_secret (:client-secret client-config)
                              :grant_type "authorization_code"
                              :redirect_uri (format-config-uri client-config)}}})

(defn- get-google-workflow
  [config db]
  (if (:use-google-auth config)
    (let [client-config (build-client-config config)]
      [(oauth2/workflow
        {:client-config client-config
         :uri-config (build-uri-config client-config)
         :credential-fn (partial lookup-roles db)})])))

(defn- login-failure [_]
  {:status 403 :headers {} :body "User or password incorrect."})

(defn- get-basic-workflow
  [config db]
  (if (:use-basic-auth config)
    (do
      (log/warn "Basic authentication enabled! This should ONLY be for dev/test.")
      (let [user (get-in config [:basic-auth-user] "admin")
            pwd (creds/hash-bcrypt (get-in config [:basic-auth-pwd] "admin"))
            users {"admin" {:username user :password pwd :roles (lookup-roles db "admin")}}
            bcrypt (partial creds/bcrypt-credential-fn users)]
        [(workflows/interactive-form
           :credential-fn bcrypt
           :login-failure-handler login-failure
           :realm "/"
           :redirect-on-auth? false)]))))

(defn- get-workflows
  [config db]
  (let [workflows (concat (get-google-workflow config db) (get-basic-workflow config db))]
    (if (= 0 (count workflows))
      (throw (Exception. "No authenication mode enabled"))
      workflows)))

(defn get-handler
  [config db]
  (let [workflows (get-workflows config db)]
    (fn [ring-handler]
      (log/info "Loading workflows" (count workflows))
      (friend/authenticate ring-handler
                           {:allow-anon? true
                            :workflows workflows}))))

(defrecord AuthProvider [config db handler]
  component/Lifecycle

  (start [this]
    (log/info "Setting up authentication component")
    (if (not db) (throw (Exception. "Authentication component requires missing 'db' dependency.")))
    (if handler
      this
      (assoc this :handler (get-handler config db))))

  (stop [this]
    (log/info "Cleaning up authentication component")
    (if (not handler)
      this
      (assoc this :handler nil))))

(defn new-authprovider
  "This class implements the Lifecycle interface providing start/stop methods
  for the authentication component for this system."
  [config]
  (map->AuthProvider {:config config}))