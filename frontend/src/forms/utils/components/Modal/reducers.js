import {createContextValue} from "App/utils/utils";
import {SHOW_MODAL} from "./constants";

export const modals = {
    modal: createContextValue(SHOW_MODAL, {isOpen: false})
};