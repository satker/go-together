import React, {useState} from "react";
import {fetchAndSetToken} from "./utils/api/request";
import {CSRF_TOKEN, USER_ID,} from "./utils/constants";
import {get as getCookie} from 'js-cookie'
import {capitalizeFirstLetter, onChange} from "./utils/utils";

export const context = {
    userId: getCookie(USER_ID),
    eventId: null,
    titleName: 'Events',
    fetchWithToken: fetchAndSetToken(getCookie(CSRF_TOKEN)),
    arrivalDate: null,
    departureDate: null,
    page: 0,
    pageSize: 9
};

export const Context = React.createContext({});

export const Provider = ({children}) => {
    const [state, setState] = useState({...context});

    return <Context.Provider value={[state, onChange(state, setState)]}>
        {children}
    </Context.Provider>;
};

export const actionFunction = (state, setState, action, setToContext, methodAction) => (...args) =>
    action(state, setState)(...args)(state.fetchWithToken(setToContext, methodAction));

const actionsStore = {};

const wrapActions = (actions, state, setState, FORM_ID) => {
    const result = {};
    for (const action in actions) {
        let methodAction;
        let path;
        if (action.startsWith('get')) {
            methodAction = 'GET';
            path = capitalizeFirstLetter(action.replace('get', ''));
        } else if (action.startsWith('post')) {
            methodAction = 'POST';
            path = capitalizeFirstLetter(action.replace('post', ''));
        } else if (action.startsWith('put')) {
            methodAction = 'PUT';
            path = capitalizeFirstLetter(action.replace('put', ''));
        } else if (action.startsWith('delete')) {
            methodAction = 'DELETE';
            path = capitalizeFirstLetter(action.replace('delete', ''));
        } else if (action.startsWith('set')) {
            path = capitalizeFirstLetter(action.replace('set', ''));
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
            if (methodAction && path) {
                const setToContext = (result) => {
                    console.log(state, path);
                    setState(FORM_ID + '.' + path, result);
                };
                result[action] = actionFunction(state, setState, actions[action], setToContext, methodAction);
                actionsStore[FORM_ID] = {
                    ...actionsStore[FORM_ID],
                    [action]: result[action]
                };
            }
        } else {
            result[action] = actionsStore[FORM_ID][action];
        }
    }
    return result;
};

export const connect = (mapStateToProps = () => null, actions = {}, FORM_ID) => (Component) => (props) => (
    <Context.Consumer>
        {([state, setState]) => (
            <Component {...props}{...mapStateToProps(state)}
                       {...wrapActions(actions, state, setState, FORM_ID)}
            />)}
    </Context.Consumer>
);