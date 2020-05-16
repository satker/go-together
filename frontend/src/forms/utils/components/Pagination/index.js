import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Pagination from '@material-ui/lab/Pagination';

const useStyles = makeStyles((theme) => ({
    root: {
        '& > *': {
            marginTop: theme.spacing(2),
        },
    },
}));

const CustomPagination = ({page, setPage, pageCount}) => {
    const classes = useStyles();

    const handleChange = (event, value) => {
        setPage(value);
    };

    return (
        <div className={classes.root}>
            <Pagination page={page}
                        onChange={handleChange}
                        count={pageCount}
                        color="primary"/>
        </div>
    );
};

export default CustomPagination;