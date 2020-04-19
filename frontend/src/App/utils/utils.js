export const createEmptyResponse = (defaultProcess = true, defaultResponse = {}) => ({
    inProcess: defaultProcess,
    response: defaultResponse,
    error: ''
});