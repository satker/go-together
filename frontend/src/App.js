import React from 'react';
import './App.css';

import {useRedirect, useRoutes} from 'hookrouter';
import Events from "./Events";
import Event from "./Event";
import PersonalArea from "./PersonalArea";
import NavBar from "./NavBar";
import FormRegister from "./Register";
import {Provider} from "./Context";

const routers = {
    '/events': () => <Events/>,
    '/events/:id': ({id}) => <Event id={id} isView={true}/>,
    '/events/:id/edit': ({id}) => <Event isView={false} id={id}/>,
    '/register': () => <FormRegister/>,
    '/home': () => <PersonalArea/>,
    '/create': () => <Event isView={false}/>
};

const App = () => {
    const route = useRoutes(routers);
    useRedirect('/', '/events');
    console.log('rerender main');
    return <Provider>
        <NavBar/>
        <div className="Content">{route}</div>
    </Provider>;
};

export default App;
