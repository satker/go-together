import React, {useState} from "react";
import {fetchAndSetToken} from "../utils/api/request";
import {CSRF_TOKEN, USER_ID,} from "../../forms/utils/constants";
import {get as getCookie} from 'js-cookie'
import {capitalizeFirstLetter, onChange} from "../../forms/utils/utils";
import {createEmptyResponse} from "../utils/utils";
import {isEmpty, keys, values} from "lodash";

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

export const actionFunction = (state, setState, action, setToContext, methodAction, FORM_ID, path) => (...args) =>
    action(state, setState)(...args)(state.fetchWithToken(setToContext, methodAction, state, FORM_ID, path));

const actionsStore = {};
export const initState = {};

const wrapActions = (actions, state, setState, FORM_ID) => {
    //const newState = initMapStateToProps(mapStateToProps, state, FORM_ID, setState);

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
                    setState(FORM_ID + '.' + path, result);
                };
                result[action] =
                    actionFunction(state, setState, actions[action], setToContext, methodAction, FORM_ID, path);
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

const initMapStateToProps = (mapStateToProps, state, FORM_ID, setState) => {
    const props = mapStateToProps(FORM_ID)(state);
    const initPropsToSet = {};
    const resultProps = {};
    for (const prop in props) {
        if (initState[FORM_ID]?.[prop]) {
            if (!props[prop]?.response) {
                props[prop] = initState[FORM_ID][prop];
            }
        } else if (props[prop] && !initState[FORM_ID]?.[prop]) {
            if (props[prop] instanceof Object || props[prop] instanceof Array) {
                if (!props[prop] instanceof Array &&
                    props[prop].hasAttribute('inProcess') &&
                    props[prop].hasAttribute('response') &&
                    props[prop].hasAttribute('error')) {
                    initPropsToSet[FORM_ID + '.' + prop] = props[prop];
                } else {
                    initPropsToSet[FORM_ID + '.' + prop] = createEmptyResponse(false, props[prop]);
                }
            }
        } else if (props[prop] === undefined) {
            initPropsToSet[FORM_ID + '.' + prop] = createEmptyResponse(false, {})
        }
    }
    if (!isEmpty(initPropsToSet)) {
        setState(keys(initPropsToSet), values(initPropsToSet));
        for (const initProp in initPropsToSet) {
            const path = initProp.replace(FORM_ID + '.', '');
            resultProps[path] = initPropsToSet[initProp];
            delete props[path];
        }
        initState[FORM_ID] = {...initState[FORM_ID], ...resultProps};
    }
    if (FORM_ID.startsWith('Autosuggestion')) {
        console.log({...resultProps, ...props})
    }
    return {...resultProps, ...props};
};

export const connect = (mapStateToProps = () => null, actions = {}) => (Component) => (FORM_ID) => (props) => {
    return (
        <Context.Consumer>
            {([state, setState]) => {
                return (
                    <Component {...props}
                               {...initMapStateToProps(mapStateToProps, state, FORM_ID, setState)}
                               {...wrapActions(actions, state, setState, FORM_ID)}
                    />)
            }}
        </Context.Consumer>
    );
};