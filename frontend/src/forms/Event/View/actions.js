import {EVENT_SERVICE_URL, EVENTS_URL} from "forms/utils/constants";
import {EVENT_VIEW_EVENT, EVENT_VIEW_STATUSES, EVENT_VIEW_USERS} from "./constants";
import {POST} from "App/utils/api/constants";

export const getEvent = (id) => (dispatch) => {
    dispatch({
        type: EVENT_VIEW_EVENT,
        url: EVENTS_URL + "/" + id
    });
};

export const getStatuses = (id) => (dispatch) => {
    dispatch({
        type: EVENT_VIEW_STATUSES,
        url: EVENT_SERVICE_URL + '/events/' + id + '/users/statuses'
    });
};

export const getUsers = (eventId) => (dispatch) => {
    dispatch({
        type: EVENT_VIEW_USERS,
        method: POST,
        url: EVENT_SERVICE_URL + '/find',
        data: {
            mainIdField: "eventUser",
            filters: {
                eventId: {
                    filterType: 'EQUAL',
                    values: [{
                        eventId: eventId
                    }]
                }
            }
        }
    });
};
