import React, {useEffect, useState} from 'react';
import {useRedirect, useRoutes} from 'hookrouter';

import Events from "forms/Events";
import Event from "forms/Event";
import PersonalArea from "forms/PersonalArea";
import FormRegister from "forms/Register";
import {Context, context} from "App/Context";
import {onChange} from "forms/utils/utils";
import {FORM_ID as FORM_ID_EVENTS} from "forms/Events/constants";
import {FORM_ID as FORM_ID_EVENT_VIEW} from "forms/Event/View/constants";
import {FORM_ID_CREATE, FORM_ID_EDIT} from "forms/Event/Edit/constants";
import {FORM_ID as FORM_ID_REGISTER} from "forms/Register/constants";
import {FORM_ID as FORM_ID_PERSONAL_AREA} from "forms/PersonalArea/constants";
import {FORM_ID as FORM_ID_NOTIFICATIONS} from "forms/Notifications/constants";

import NavBar from "./NavBar";
import Notifications from "forms/Notifications";
import Notification from "forms/utils/components/Notification";
import ModalWindow from "forms/utils/components/Modal";

const routers = {
    '/events': () => <Events key={FORM_ID_EVENTS}/>,
    '/events/:id': ({id}) => <Event id={id} isView={true} key={FORM_ID_EVENT_VIEW}/>,
    '/events/:id/edit': ({id}) => <Event isView={false} id={id} key={FORM_ID_EDIT}/>,
    '/register': () => <FormRegister key={FORM_ID_REGISTER}/>,
    '/home': () => <PersonalArea key={FORM_ID_PERSONAL_AREA}/>,
    '/create': () => <Event isView={false} key={FORM_ID_CREATE}/>,
    '/notifications': () => <Notifications key={FORM_ID_NOTIFICATIONS}/>
};

const App = () => {
    const route = useRoutes(routers);
    const [state, setState] = useState({...context});

    useEffect(() => {
        if (route.key && route.key !== state.formId.value) {
            const interval = state.temporary.interval.value;
            if (interval.length) {
                interval.forEach(timer => clearInterval(timer));
                onChange(state, setState)(['formId.value', 'temporary.interval.value'], [route.key, []]);
            } else {
                onChange(state, setState)('formId.value', route.key);
            }
        }
    }, [route, state, setState]);

    useRedirect('/', '/events');

    return state.formId.value && <Context.Provider value={[state, onChange(state, setState)]}>
        <NavBar/>
        <Notification/>
        <ModalWindow/>
        {route}
    </Context.Provider>;
};

export default App;
