import {EVENT_SERVICE_URL} from "../../../utils/constants";

export const getHousingTypes = () => () => (fetch) => {
    fetch(EVENT_SERVICE_URL + '/events/housingTypes', () => null);
};