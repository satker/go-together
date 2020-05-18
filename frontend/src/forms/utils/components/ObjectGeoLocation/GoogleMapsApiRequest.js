import {GET} from "App/utils/api/constants";

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
