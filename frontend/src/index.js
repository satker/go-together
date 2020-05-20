import React from 'react';
import ReactDOM from 'react-dom';
import App from 'App';

import unregister from './RegisterServiceWorker';

import './index.css';


ReactDOM.render(
    <App/>, document.getElementById('root'));

unregister();
