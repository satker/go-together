import React from "react";
import {DateTimePicker, MuiPickersUtilsProvider,} from '@material-ui/pickers';
import DateFnsUtils from '@date-io/moment';
import {Grid} from "@material-ui/core";

const SingleDate = ({date, onChange, label}) => {
    return <MuiPickersUtilsProvider utils={DateFnsUtils}>
        <Grid container justify="space-around">
            <DateTimePicker
                label={label}
                value={date}
                onChange={onChange}
                showTodayButton
                disablePast
            />
        </Grid>
    </MuiPickersUtilsProvider>
};

export default SingleDate;