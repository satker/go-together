import React, {useState} from 'react';
import './App.css';

import {useRedirect, useRoutes} from 'hookrouter';
import Events from "./Events";
import Event from "./Event";
import PersonalArea from "./PersonalArea";
import NavBar from "./NavBar";
import FormRegister from "./Register";
import {Context, context} from "./Context";
import {onChange} from "./utils/utils";

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
    const [state, setState] = useState({...context});

    return <Context.Provider value={[state, onChange(state, setState)]}>
        <NavBar/>
        <div className="Content">{route}</div>
    </Context.Provider>;
};

export default App;
