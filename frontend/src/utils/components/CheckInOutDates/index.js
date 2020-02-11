import React, {useState} from 'react';
import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';
import {DateRangePicker, START_DATE} from "react-dates";

const CheckInOutDates = ({startDate, endDate, setStartDate, setEndDate}) => {
    const [focusedInput, setFocusedInput] = useState(START_DATE);
    return <DateRangePicker
        small={true}
        startDate={startDate} // momentPropTypes.momentObj or null,
        startDateId="your_unique_start_date_id" // PropTypes.string.isRequired,
        endDate={endDate} // momentPropTypes.momentObj or null,
        endDateId="your_unique_end_date_id" // PropTypes.string.isRequired,
        onDatesChange={({startDate, endDate}) => {
            setStartDate(startDate);
            setEndDate(endDate);
        }} // PropTypes.func.isRequired,
        focusedInput={focusedInput} // PropTypes.oneOf([START_DATE, END_DATE]) or null,
        onFocusChange={focusedInput => setFocusedInput(focusedInput)} // PropTypes.func.isRequired,
    />
};

export default CheckInOutDates;