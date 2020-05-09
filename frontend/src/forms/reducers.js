import {utils} from './utils/reducers'
import {personalArea} from "./PersonalArea/reducers";
import {events} from "./Events/reducers";
import {event} from "./Event/reducers";
import {register} from "./Register/reducers";
import {login} from "./Login/reducers";

export const components = {
    utils,
    forms: {
        register,
        personalArea,
        events,
        event,
        login
    }
};