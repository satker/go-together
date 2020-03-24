import React from "react";
import {fetchAndSetToken} from "./utils/api/request";
import {CSRF_TOKEN, USER_ID,} from "./utils/constants";
import {get as getCookie} from 'js-cookie'

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
