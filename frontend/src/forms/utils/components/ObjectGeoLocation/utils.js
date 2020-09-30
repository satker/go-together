import {GET} from "App/utils/api/constants";
import Marker from "./Marker";
import React from "react";

export const GOOGLE_API_KEY = "AIzaSyDDOYr_mg4-0iLXHExVIHte0QaDZ50QHcs";

const resolveStatus = (response) => {
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
};

const resolveJson = (response) => {
    return response.json();
};

export const requestLatLng = (lat, lng, resultFunction) => {
    const url = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}&key=${GOOGLE_API_KEY}&language=en`;

    fetch(url, {method: GET})
        .then(resolveStatus)
        .then(resolveJson)
        .then(resultFunction);
};

export const requestPlaceId = (place, resultFunction) => {
    const url = `https://maps.googleapis.com/maps/api/geocode/json?place_id=${place.id}&key=${GOOGLE_API_KEY}&language=en`;

    fetch(url, {method: GET})
        .then(resolveStatus)
        .then(resolveJson)
        .then(resultFunction(place));
};

export const getAddress = (addressObject) => {
    return addressObject.formatted_address;
};

export const getCity = (addressObject) => {
    const addressArray = addressObject.address_components;
    let city = '';
    for (let i = 0; i < addressArray.length; i++) {
        if (addressArray[i].types[0] && 'administrative_area_level_2' === addressArray[i].types[0]) {
            city = addressArray[i].long_name;
            return city;
        }
    }
};

export const getCountry = (addressObject) => {
    const addressArray = addressObject.address_components;
    let country = '';
    for (let i = 0; i < addressArray.length; i++) {
        for (let i = 0; i < addressArray.length; i++) {
            if (addressArray[i].types[0] && 'country' === addressArray[i].types[0]) {
                country = addressArray[i].long_name;
                return country;
            }
        }
    }
};

export const getState = (addressObject) => {
    const addressArray = addressObject.address_components;
    let state = '';
    for (let i = 0; i < addressArray.length; i++) {
        for (let i = 0; i < addressArray.length; i++) {
            if (addressArray[i].types[0] && 'administrative_area_level_1' === addressArray[i].types[0]) {
                state = addressArray[i].long_name;
                return state;
            }
        }
    }
};

export const getMarker = (routes, onClick) => {
    return routes
        .map(route => <Marker
            onClick={() => onClick(route)}
            key={route.routeNumber}
            lat={route.latitude}
            lng={route.longitude}
            name={route.address}
            color={getColorByRouteNumber(route, routes)}
        />);
}

const getColorByRouteNumber = (route, routes) => {
    if (route.routeNumber === 1) {
        return 'green';
    } else if (route.routeNumber === routes.length) {
        return 'red'
    } else {
        return 'orange';
    }
};

export const sort = (locations) => locations.sort((route1, route2) => route1.routeNumber > route2.routeNumber ? 1 : -1);