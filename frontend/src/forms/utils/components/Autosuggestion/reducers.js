import {createEmptyResponse} from "App/utils/utils";
import {AUTOSUGGESTION_OPTIONS_EVENTS, AUTOSUGGESTION_OPTIONS_LOCATIONS} from "./constants";

export const autosuggestion = {
    options: {
        locations: createEmptyResponse(AUTOSUGGESTION_OPTIONS_LOCATIONS, []),
        events: createEmptyResponse(AUTOSUGGESTION_OPTIONS_EVENTS, [])
    }
};