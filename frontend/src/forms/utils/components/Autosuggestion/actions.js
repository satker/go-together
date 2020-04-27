export const getOptions = (reducerParam) => () => (url, urlParam, value) => (dispatch) => {
    dispatch(url + '?' + urlParam + '=' + value)(reducerParam);
};