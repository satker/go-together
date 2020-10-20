import React from "react";

import {fetchAndSetToken} from "App/utils/api/request";
import {components} from "forms/reducers";
import {createContextValue} from "App/utils/utils";
import {AUTH, CSRF_TOKEN, EVENT_ID, FORM_ID, PAGE, PAGE_SIZE, USER_ID} from "./constants";
import {temporary} from "App/TemporaryTimer/reducers";

export const context = {
    auth: createContextValue(AUTH, {
        userId: localStorage.getItem(USER_ID),
        csrfToken: localStorage.getItem(CSRF_TOKEN)
    }),
    eventId: createContextValue(EVENT_ID),
    formId: createContextValue(FORM_ID),
    page: createContextValue(PAGE, 0),
    pageSize: createContextValue(PAGE_SIZE, 9),
    temporary,
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
    const dispatch = fetchAndSetToken(state.auth.value.csrfToken)(setToContext(setState));
    for (const action in actions) {
        if (!(actionsStore[FORM_ID] && actionsStore[FORM_ID][action])) {
            result[action] = (...args) => actions[action](...args)(dispatch, state);
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