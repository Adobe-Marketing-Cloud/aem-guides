import React, {Component} from 'react';
import {MapTo} from '@adobe/cq-react-editable-components';

require('./HelloWorld.css');

class HelloWorld extends Component {

    render() {

        let displayMessage;

        if(this.props.cqModel) {
            displayMessage = this.props.cqModel.displayMessage;

            return (
                <div className="cmp-helloworld">
                <h1 className="cmp-helloworld--message">{displayMessage}</h1>
                </div>
            );
        }

        return null;
    }
}

const HelloWorldEditConfig = {

    emptyLabel: 'Hello World',

    isEmpty: function() {
        return !this.props || !this.props.cqModel || !this.props.cqModel.displayMessage || this.props.cqModel.displayMessage.trim().length < 1;
    }
};

MapTo('we-retail-journal/global/components/helloworld')(HelloWorld, HelloWorldEditConfig);