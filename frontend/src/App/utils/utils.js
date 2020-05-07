import {keys} from "lodash";

export const createEmptyResponse = (type, defaultResponse = {}, defaultProcess = false) => ({
    type,
    inProcess: defaultProcess,
    response: defaultResponse,
    error: ''
});

export const findPath = (findType, path, components) => {
    for (const component of keys(components)) {
        let resultPath = null;
        let currentPath = (path ? path + '.' : '') + component;
        if (components[component].type === findType) {
            return [currentPath, components[component]];
        }
        if (!components[component].hasOwnProperty('inProcess') && components[component] instanceof Object) {
            resultPath = findPath(findType, currentPath, components[component])
        }
        if (resultPath) {
            return resultPath;
        }
    }
};