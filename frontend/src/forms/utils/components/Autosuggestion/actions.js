export const getOptions = (reducerParam) => (url, urlParam, value) => (dispatch) => {
    dispatch({
        type: reducerParam,
        url: url + '?' + urlParam + '=' + value
    });
};