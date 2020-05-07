import React, {useState} from "react";
import {fetchAndSetToken} from "../utils/api/request";
import {CSRF_TOKEN, USER_ID,} from "../../forms/utils/constants";
import {get as getCookie} from 'js-cookie'
import {onChange} from "../../forms/utils/utils";
import {components} from "../../forms/reducers";

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

const wrapActions = (actions, state, setState, ACTIONS_ID) => {
    if (!actions) {
        return {};
    }
    const FORM_ID = ACTIONS_ID || 'actions';

    const result = {};
    for (const action in actions) {
        if (!(actionsStore[FORM_ID] && actionsStore[FORM_ID][action])) {
            const setToContext = (result, path) => {
                let statePath = 'components.' + path;
                setState(statePath, {...result});
            };
            result[action] = (...args) =>
                actions[action](state, setState)(...args)(state.fetchWithToken(setToContext));
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
                       {...(mapStateToProps ? mapStateToProps(ACTIONS_ID)(state) : {})}
                       {...wrapActions(actions, state, setState, ACTIONS_ID)}
            />}
    </Context.Consumer>;