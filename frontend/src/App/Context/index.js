import React, {useState} from "react";
import {fetchAndSetToken} from "../utils/api/request";
import {CSRF_TOKEN, USER_ID,} from "../../forms/utils/constants";
import {get as getCookie} from 'js-cookie'
import {onChange} from "../../forms/utils/utils";
import {components} from "../../forms/reducers";
import {DELETE, GET, POST, PUT} from "../utils/api/constants";

export const context = {
    userId: getCookie(USER_ID) === 'null' ? null : getCookie(USER_ID),
    eventId: null,
    formId: null,
    fetchWithToken: fetchAndSetToken(getCookie(CSRF_TOKEN)),
    arrivalDate: null,
    departureDate: null,
    page: 0,
    pageSize: 9,
    components: {...components}
};

export const Context = React.createContext({});

const actionsStore = {};

export const Provider = ({children}) => {
    const [state, setState] = useState({...context});

    return <Context.Provider value={[state, onChange(state, setState)]}>
        {children}
    </Context.Provider>;
};

export const actionFunction = (state, setState, action, setToContext, methodAction) => (...args) =>
    action(state, setState)(...args)(state.fetchWithToken(setToContext, methodAction));

const wrapActions = (actions, state, setState, FORM_ID) => {
    if (!actions) {
        return {};
    }
    const result = {};
    for (const action in actions) {
        let methodAction;
        if (action.startsWith(GET.toLowerCase())) {
            methodAction = GET;
        } else if (action.startsWith(POST.toLowerCase())) {
            methodAction = POST;
        } else if (action.startsWith(PUT.toLowerCase())) {
            methodAction = PUT;
        } else if (action.startsWith(DELETE.toLowerCase())) {
            methodAction = DELETE;
        } else if (action.startsWith('set')) {
            if (!(actionsStore[FORM_ID] && actionsStore[FORM_ID][action])) {
                result[action] = (...args) => actions[action](state, setState)(...args)();
                actionsStore[FORM_ID] = {
                    ...actionsStore[FORM_ID],
                    [action]: result[action]
                };
            } else {
                result[action] = actionsStore[FORM_ID][action];
            }
        }
        if (!(actionsStore[FORM_ID] && actionsStore[FORM_ID][action])) {
            if (methodAction) {
                const setToContext = (result, path) => {
                    let statePath = 'components.' + path;
                    setState(statePath, {...result});
                };
                result[action] =
                    actionFunction(state, setState, actions[action], setToContext, methodAction);
                actionsStore[FORM_ID] = {
                    ...actionsStore[FORM_ID],
                    [action]: result[action]
                };
            }
        } else {
            result[action] = actionsStore[FORM_ID][action];
        }
    }
    return {...result};
};

export const connect = (mapStateToProps, actions) => (Component) => (FORM_ID) => (props) =>
    <Context.Consumer>
        {([state, setState]) =>
            <Component {...props}
                       {...(mapStateToProps ? mapStateToProps(FORM_ID)(state) : {})}
                       {...wrapActions(actions, state, setState, FORM_ID)}
            />}
    </Context.Consumer>;