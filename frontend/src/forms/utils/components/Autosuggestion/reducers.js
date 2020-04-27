import {createEmptyResponse} from "../../../../App/utils/utils";

export const autosuggestion = {
    options: {
        locations: createEmptyResponse(false, []),
        events: createEmptyResponse(false, [])
    }
};