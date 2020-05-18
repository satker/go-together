import {EVENT_SERVICE_URL} from "forms/utils/constants";
import {EDIT_MAIN_INFO_HOUSING_TYPES} from "./constants";

export const getHousingTypes = () => (dispatch) => {
    dispatch({
        type: EDIT_MAIN_INFO_HOUSING_TYPES,
        url: EVENT_SERVICE_URL + '/events/housingTypes'
    });
};