import {autosuggestion} from "./reducers";

export const getOptions = () => (url, urlParam, value) => (dispatch) => {
    dispatch(url + '?' + urlParam + '=' + value)(autosuggestion.options);
};