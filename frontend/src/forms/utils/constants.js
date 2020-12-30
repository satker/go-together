export const HOST = 'localhost';
export const GATEWAY_URL = 'http://' + HOST + ':8080';
export const GATEWAY_SERVICES_URL = GATEWAY_URL + '/services';
export const AUTH_URL = 'http://' + HOST + ':8088';

export const EVENT_SERVICE_URL = GATEWAY_SERVICES_URL + '/event-service';
export const USER_SERVICE_URL = GATEWAY_SERVICES_URL + '/user-service';
export const CONTENT_SERVICE_URL = GATEWAY_SERVICES_URL + '/content-service';
export const MESSAGE_SERVICE_URL = GATEWAY_SERVICES_URL + '/message-service';
export const NOTIFICATION_SERVICE_URL = GATEWAY_SERVICES_URL + '/notification-service';
export const LOCATION_SERVICE_URL = GATEWAY_SERVICES_URL + '/location-service';
export const ROUTE_INFO_SERVICE_URL = GATEWAY_SERVICES_URL + '/route-info-service';
export const LOGIN_URL = AUTH_URL + "/auth/";

export const EVENTS_URL = EVENT_SERVICE_URL + '/events';

export const DEFAULT_MAIN_INFO = {
    id: null,
    name: '',
    description: '',
    startDate: null,
    endDate: null,
    peopleCount: 0,
    groupPhoto: {
        photos: []
    }
};

export const DEFAULT_COUNTRY = {
    id: null,
    countryCode: null,
    name: null
};

export const PLACE = {
    id: null,
    name: null,
    state: null,
    country: {...DEFAULT_COUNTRY}
};

export const DEFAULT_ROUTE = {
    id: null,
    address: null,
    latitude: 18.5204,
    longitude: 73.8567,
    place: {...PLACE}
};

export const DEFAULT_ROUTE_INFO = (location, routeNumber) => ({
    location,
    transportType: '',
    cost: 0,
    movementDate: null,
    movementDuration: 0,
    routeNumber
});

export const DEFAULT_CREATE_EVENT = {
    ...DEFAULT_MAIN_INFO,
    routeInfo: {
        groupId: null,
        infoRoutes: []
    }
};

export const SIMPLE_USER = {
    id: null,
    login: null,
    firstName: null,
    lastName: null,
    userPhoto: null
};

export const DEFAULT_EVENT_USER = {
    id: null,
    user: SIMPLE_USER,
    userStatus: 'WAITING_APPROVE',
    eventId: null
};

export const FORM_DTO = (mainFieldId) => ({
    page: {
        page: 0,
        size: 10,
        totalSize: 0,
        sort: []
    },
    mainIdField: mainFieldId,
    filters: {}
});

export const FILTER_DTO = {
    filterType: null,
    values: []
};

export const PHOTO_OBJECT = {
    id: null,
    photoUrl: null,
    content: {
        type: null,
        photoContent: null
    }
};