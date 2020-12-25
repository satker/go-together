import {TEMPORARY_TIMER} from "./constants";

export const setTimer = (intervalId) => (dispatch, state) => {
    const intervals = state.temporary.interval.value;
    dispatch({
        type: TEMPORARY_TIMER,
        value: [...intervals, intervalId]
    });
};

export const clearTimer = (intervalId) => (dispatch, state) => {
    const intervals = state.temporary.interval.value;
    clearInterval(intervalId);
    dispatch({
        type: TEMPORARY_TIMER,
        value: intervals.filter(interval => interval !== intervalId)
    });
};

export const clearTimers = () => (dispatch, state) => {
    const intervals = state.temporary.interval.value;
    intervals.forEach(intervalId => clearInterval(intervalId));
    dispatch({
        type: TEMPORARY_TIMER,
        value: []
    });
};