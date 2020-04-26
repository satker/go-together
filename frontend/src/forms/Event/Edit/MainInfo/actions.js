import {EVENT_SERVICE_URL} from "../../../utils/constants";
import {commonInfo} from "./CommonInfo/reducers";

export const getHousingTypes = () => () => (dispatch) => {
    dispatch(EVENT_SERVICE_URL + '/events/housingTypes')
    (commonInfo.housingTypes);
};