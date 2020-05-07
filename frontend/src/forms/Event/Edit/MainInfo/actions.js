import {EVENT_SERVICE_URL} from "../../../utils/constants";
import {EDIT_MAIN_INFO_HOUSING_TYPES} from "./constants";

export const getHousingTypes = () => () => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/housingTypes')
    (EDIT_MAIN_INFO_HOUSING_TYPES);
};