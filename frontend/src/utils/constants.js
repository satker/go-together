export const GATEWAY_URL = 'http://localhost:8080';
export const GATEWAY_SERVICES_URL = GATEWAY_URL + '/services';
export const AUTH_URL = 'http://localhost:8088';

export const EVENT_SERVICE_URL = GATEWAY_SERVICES_URL + '/event-service';
export const USER_SERVICE_URL = GATEWAY_SERVICES_URL + '/user-service';
export const CONTENT_SERVICE_URL = GATEWAY_SERVICES_URL + '/content-service';
export const MESSAGE_SERVICE_URL = GATEWAY_SERVICES_URL + '/message-service';
export const LOCATION_SERVICE_URL = GATEWAY_SERVICES_URL + '/location-service';
export const LOGIN_URL = AUTH_URL + "/auth/";

export const CSRF_TOKEN = 'CSRF_TOKEN';
export const USER_ID = 'USER_ID';

export const EVENTS_URL = EVENT_SERVICE_URL + '/events';

export const DEFAULT_MAIN_INFO = {
    id: null,
    name: null,
    description: null,
    housingType: null,
    startDate: null,
    endDate: null,
    peopleCount: 0,
    eventPhotoDto: {
        photos: []
    }

};

export const DEFAULT_COUNTRY = {
    id: null,
    countryCode: null,
    name: null
};

export const DEFAULT_LOCATION = {
    id: null,
    name: null,
    state: null,
    country: {...DEFAULT_COUNTRY}
};

export const DEFAULT_ROUTE = {
    id: null,
    routeNumber: 0,
    address: null,
    latitude: 18.5204,
    longitude: 73.8567,
    location: {...DEFAULT_LOCATION}
};

export const DEFAULT_CREATE_EVENT = {
    ...DEFAULT_MAIN_INFO,
    paidThings: [],
    route: []
};

export const DEFAULT_PAID_THING = {
    id: null,
    cashCategory: null,
    paidThing: {
        id: null,
        name: null
    }
};

export const LOCATION_DEFAULT = null;
export const MIN_COST_DEFAULT = 0;
export const MAX_COST_DEFAULT = 100;
export const ADULTS_DEFAULT = 0;
export const CHILDREN_DEFAULT = 0;
export const START_DATE_DEFAULT = null;
export const END_DATE_DEFAULT = null;
export const BEDS_DEFAULT = 0;
export const ROOMS_DEFAULT = 0;
export const EMPTY_ARRAY = [];

export const SEARCH_OBJECT_DEFAULT = {
    minCostNight: MIN_COST_DEFAULT,
    maxCostNight: MAX_COST_DEFAULT,
    location: LOCATION_DEFAULT,
    arrivalDate: START_DATE_DEFAULT,
    departureDate: END_DATE_DEFAULT,
    adult: ADULTS_DEFAULT,
    children: CHILDREN_DEFAULT,
    advancedSearch: {
        beds: BEDS_DEFAULT,
        rooms: ROOMS_DEFAULT,
        apartmentTypes: EMPTY_ARRAY,
        parameters: EMPTY_ARRAY,
        languages: EMPTY_ARRAY
    },
    page: 0
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