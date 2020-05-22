import {GET} from "../../../../App/utils/api/constants";

export const GOOGLE_API_KEY = "AIzaSyBSjnMkN8ckymUWZO5v0q-cZW9WppoFsyM";

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

export const request = (lat, lng, resultFunction) => {
    const url = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}&key=${GOOGLE_API_KEY}&language=en`;

    fetch(url, {method: GET})
        .then(resolveStatus)
        .then(resolveJson)
        .then(resultFunction);
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