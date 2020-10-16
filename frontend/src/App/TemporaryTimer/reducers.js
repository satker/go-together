import {createContextValue} from "../utils/utils";
import {TEMPORARY_TIMER} from "./constants";

export const temporary = {
    interval: createContextValue(TEMPORARY_TIMER, [])
}