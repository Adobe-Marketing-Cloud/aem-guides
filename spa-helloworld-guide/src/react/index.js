/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2018 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import {PageModelManager} from '@adobe/cq-react-editable-components';
import {BrowserRouter} from 'react-router-dom';

require('./components/Text');
require('./components/Image');
require('./components/HelloWorld');
require('./components/Weather');
require('./components/Navigation');
require('./components/Page');

function render() {
    // Using HashRouter for now as it's easier to deal with hashes in the location + we are serving static content (while BrowserRouter is a better fit for serving dynamic content)
    ReactDOM.render((<BrowserRouter>
        <App/>
    </BrowserRouter>), document.getElementById('page'));
}

// Use a different path when we are using a remote environment
if(process.env.NODE_ENV === 'development') {
    // Pre-loading the page model before rendering the app
    PageModelManager.init(process.env.REACT_APP_PAGE_MODEL_PATH).then(render);
} else {
    // Instantly render the app and let the default setup apply
    render();
}
