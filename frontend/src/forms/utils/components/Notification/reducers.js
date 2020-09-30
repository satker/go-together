import {createContextValue} from "App/utils/utils";
import {NOTIFICATION} from "./constants";

export const notifications = {
    notification: createContextValue(NOTIFICATION, {isOpen: false})
};