import React, {useState} from 'react';
import {DateRangePicker, START_DATE} from "react-dates";

import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';

const CheckInOutDates = ({startDate, endDate, setStartDate, setEndDate, readOnly}) => {
    const [focusedInput, setFocusedInput] = useState(START_DATE);
    return <DateRangePicker
        readOnly={readOnly}
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