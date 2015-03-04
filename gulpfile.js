var browserify = require('browserify');
var gulp = require('gulp');
var autoprefixer = require('gulp-autoprefixer');
var minifyCss = require('gulp-minify-css');
var rename = require('gulp-rename');
var streamify = require('gulp-streamify');
var uglify = require('gulp-uglify');
var reactify = require('reactify');
var sass = require('gulp-sass');
var source = require('vinyl-source-stream');
var watchify = require('watchify');

function scripts(watch) {
    var bundler = browserify('./src/main/js/planted.js', {
        // Gives us source maps for nice debugging
        debug: true,

        // Required for watchify
        cache: {},
        packageCache: {},

        // Needs to be true for watchify
        fullPaths: watch
    });

    if (watch) {
        bundler = watchify(bundler);
    }

    // Process JSX transforms
    bundler.transform(reactify);

    var rebundle = function() {
      return bundler.bundle()
          .pipe(source('planted.js'))
          .pipe(gulp.dest('./resources/public/js'))
          .pipe(streamify(uglify()))
          .pipe(rename({extname: ".min.js"}))
          .pipe(gulp.dest('./resources/public/js'));
    };

    bundler.on('update', rebundle);
    return rebundle();
}

gulp.task('js', function() {
    scripts(false);
});

gulp.task('watchJs', function() {
    scripts(true);
});

gulp.task('sass', function () {
  gulp.src('./src/main/scss/planted.scss')
    .pipe(sass({errLogToConsole: true}))
    .pipe(autoprefixer())
    .pipe(gulp.dest('./resources/public/css'))
    .pipe(minifyCss())
    .pipe(rename({extname: ".min.css"}))
    .pipe(gulp.dest('./resources/public/css'));
});

gulp.task('watch', function () {
    gulp.watch('./src/main/scss/*.scss', ['sass']);
});

gulp.task('dev', ['sass', 'watch', 'watchJs'])
gulp.task('default', ['sass', 'js']);