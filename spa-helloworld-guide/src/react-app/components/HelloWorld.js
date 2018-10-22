import React, {Component} from 'react';
import {MapTo} from '@adobe/cq-react-editable-components';
require('./HelloWorld.css');

const HelloWorldEditConfig = {

    emptyLabel: 'Hello World',

    isEmpty: function(props) {
        return !props || !props.displayMessage || props.displayMessage.trim().length < 1;
    }
};

class HelloWorld extends Component {

    render() {

        if(this.props.displayMessage) {
            return (
                <div className="cmp-helloworld">
                    <h1 className="cmp-helloworld_message">{this.props.displayMessage}</h1>
                </div>
            );
        }
        return null;
    }
}

MapTo('we-retail-journal/components/helloworld')(HelloWorld, HelloWorldEditConfig);