var React = require('react');
//var baguetteBox = require('baguettebox.js');

module.exports = React.createClass({
    render: function () {
        var images = this.props.images;
        if (!images || !images.length) return (
            <div className="image-thumbs-box empty"></div>
        );

        var primary = images[0];
        var primaryIndex = 0;
        for (var i=1; i<images.length; i++) {
            if (images[i].primary) {
                primary = images[i];
                primaryIndex = i;
            }
        }

        var smaller = images.map(function(img, index) {
            if (index === primaryIndex) return;
            if ((index < primaryIndex && index >= 3) || (index > primaryIndex && index >= 4)) return;
            return (
                <a href={img.url}><img src={img.url} alt={img.caption} /></a>
            );
        });

        var smallerBlock = "";
        if (images.length > 1) {
            smallerBlock = (
                <div className="tiny-thumbs">
                {smaller}
                </div>
            );
        }

        return (
            <div className="image-thumbs-box" ref="thumbsBox">
                <a href={primary.url}><img src={primary.url} alt={primary.caption} /></a>
                {smallerBlock}
            </div>
        );
    },

    componentDidMount: function() {
        if (!this.props.images || !this.props.images.length) return;

        var container = React.findDOMNode(this.refs.thumbsBox);
        baguetteBox.run(container.id);
    }
});