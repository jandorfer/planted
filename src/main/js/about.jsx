var React = require('react');

module.exports = React.createClass({
    render: function () {
        return (
            <div className="container">
                <h1>About</h1>
                <p>
                    Planted is a tool to track growing things. Do you grow things? Good. Then
                    this could interested you.
                </p>
                <h3>Step One: Set the stage (err, garden.)</h3>
                <p>
                    Sign in and create some plants. The more information, the better (that's)
                    where the value comes in later.
                </p>
                <h3>Step Two: Wait for it</h3>
                <p>
                    Growing things takes time. After planted and noting what you did, you'll
                    have to give things a few (hours/days/weeks) to get going. Once you actually
                    see something go to Step Three.
                </p>
                <h3>Step Three: Something's happening!</h3>
                <p>
                    Finally! A little sprout. Let's write a report about it. Go click on your
                    garden and choose "New Report". Let's type "Yay! It started growing!". Then
                    you'll want to link the report to the plant it's about. It should be setup
                    by default, if not just click to link it to whatever plant or plants you want.
                    For added fun, let's add some information to the report about the plant. You
                    can add anything you want. Some ideas:
                    <ul>
                    <li>How tall is it?</li>
                    <li>What kind of soil is it in?</li>
                    <li>Shady? What percentage?</li>
                    <li>Is it healthy looking (1-10)?</li>
                    <li>How does it make you feel (1-10)?</li>
                    </ul>
                    It doesn't really matter what you enter in here, the idea is that these things
                    are information that when you look back at a fancy graph years from now you can
                    say "Oh wow, those tulips seem to make me most happy in June! That must be when
                    they typically flower in my particular garden."
                </p>
                <h3>Repeat Step Three... many times</h3>
                <p>
                    Wait some more. Things change. Things grow, things blossom, things wither and
                    die. Add reports for all of it, throwing in whatever new tidbits you want along
                    the way. Try to be consistent and get measurements for most things every time.
                </p>
                <h3>Step Four: Analysis</h3>
                <p>
                    The growing season ends, nothing much is expected to change. Write the last
                    reports you want, then start clicking around (as if you already hadn't). You
                    should be able to see charts of the various things you tracked for each plant
                    or aggregate by type of plant. Or many other things. Just play around.
                </p>
                <p>
                    With luck the information you've recorded should give you insights into how
                    your plants perform in different spots and different times of the year so that
                    next year you can go in and do it all again, with the sneaking suspicion you
                    learned something along the way.
                </p>
                <h3>Future?</h3>
                <p>
                    I'd like to expand this system to eventually track the results of the plants you
                    grow. Specifically, I grow grapes. Someday I hope to be able to report on the
                    grape quality, the wine making process and ultimately tie a bottle of wine back
                    to all this wonderful information about the plants that led to it. If you don't
                    do wine, use your imagination. Same thing could apply for the quality of your
                    apple pies.
                </p>
                <p>
                    Also, automation. It seems obvious that we should be able to tie in the local
                    weather (or better yet, a personal weather station if you have one.) Any other
                    sensors would be helpful too.
                </p>
            </div>
        );
    }
});