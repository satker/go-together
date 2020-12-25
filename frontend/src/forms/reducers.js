import {utils} from 'forms/utils/reducers'
import {personalArea} from "forms/PersonalArea/reducers";
import {events} from "forms/Events/reducers";
import {event} from "forms/Event/reducers";
import {register} from "forms/Register/reducers";
import {notifications} from "forms/Notifications/reducers";

export const components = {
    utils,
    forms: {
        register,
        personalArea,
        events,
        event,
        notifications
    }
};