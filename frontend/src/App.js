import React, {useState} from 'react';
import './App.css';

import {useRedirect, useRoutes} from 'hookrouter';
import Events from "./Events";
import Event from "./Event";
import PersonalArea from "./PersonalArea";
import NavBar from "./NavBar";
import FormRegister from "./Register";
import {Context, context} from "./Context";
import {get, set} from "lodash";

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

    const onChange = (path, value) => {
        if (path instanceof Array && value instanceof Array) {
            if (path.length === value.length) {
                const newState = {...state};
                for (let i = 0; i < path.length; i++) {
                    if (get(state, path[i]) !== value[i]) {
                        set(newState, path[i], value[i]);
                    }
                }
                setState(newState);
            }
        }
        if (path instanceof String) {
            if (get(state, path) !== value) {
                const newState = {...state};
                set(newState, path, value);
                setState(newState);
            }
        }
    };

    return <Context.Provider value={[state, onChange]}>
        <NavBar/>
        <div className="Content">{route}</div>
    </Context.Provider>;
};

export default App;
