import React from 'react';
import './index.css';

import {useRedirect, useRoutes} from 'hookrouter';
import Events from "../forms/Events";
import Event from "../forms/Event";
import PersonalArea from "../forms/PersonalArea";
import NavBar from "./NavBar";
import FormRegister from "../forms/Register";
import {Provider} from "../App/Context";

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
    return <Provider>
        <NavBar/>
        <div className="Content">{route}</div>
    </Provider>;
};

export default App;
