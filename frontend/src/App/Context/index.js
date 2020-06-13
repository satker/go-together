import React from "react";
import {get as getCookie} from 'js-cookie';

import {fetchAndSetToken} from "App/utils/api/request";
import {USER_ID} from "forms/utils/constants";
import {components} from "forms/reducers";
import {createContextValue} from "App/utils/utils";
import {CONTEXT_USER_ID, CSRF_TOKEN, EVENT_ID, FORM_ID, PAGE, PAGE_SIZE} from "./constants";

export const context = {
    userId: createContextValue(CONTEXT_USER_ID, getCookie(USER_ID) === 'null' ? null : getCookie(USER_ID)),
    eventId: createContextValue(EVENT_ID),
    formId: createContextValue(FORM_ID),
    csrfToken: createContextValue(CSRF_TOKEN, getCookie(CSRF_TOKEN) === 'null' ? null : getCookie(CSRF_TOKEN)),
    page: createContextValue(PAGE, 0),
    pageSize: createContextValue(PAGE_SIZE, 9),
    components: {...components}
};

export const Context = React.createContext({});

const actionsStore = {};

const setToContext = setState => (pathData) => setState(pathData.path, {...pathData.data});

const wrapActions = (actions, state, setState, ACTIONS_ID) => {
    if (!actions) {
        return {};
    }
    const FORM_ID = ACTIONS_ID || state.formId.value;

    const result = {};
    for (const action in actions) {
        if (!(actionsStore[FORM_ID] && actionsStore[FORM_ID][action])) {
            result[action] = (...args) =>
                actions[action](...args)(fetchAndSetToken(state.csrfToken.value)(setToContext(setState)), state);
            actionsStore[FORM_ID] = {
                ...actionsStore[FORM_ID],
                [action]: result[action]
            };
        } else {
            result[action] = actionsStore[FORM_ID][action];
        }
    }
    return {...result};
};

export const connect = (mapStateToProps, actions, ACTIONS_ID) => (Component) => (props) =>
    <Context.Consumer>
        {([state, setState]) =>
            <Component {...props}
                       {...(mapStateToProps ? mapStateToProps(state) : {})}
                       {...wrapActions(actions, state, setState, ACTIONS_ID)}
            />}
    </Context.Consumer>;