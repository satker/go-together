import React from "react";
import Toolbar from "@material-ui/core/Toolbar";
import PropTypes from "prop-types";
import Typography from "@material-ui/core/Typography";
import IconButton from "@material-ui/core/IconButton";
import {navigate} from "hookrouter";
import MoreIcon from "@material-ui/icons/MoreVert";
import AppBar from "@material-ui/core/AppBar";
import {fade, makeStyles} from "@material-ui/core/styles";
import {EVENT_SERVICE_URL} from "../../../forms/utils/constants";
import {AutosuggestionEvents} from "../../../forms/utils/components/Autosuggestion";
import ToolbarButtons from "../ToolbarButtons";

const useStyles = makeStyles(theme => ({
    grow: {
        flexGrow: 1
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        display: 'none',
        [theme.breakpoints.up('sm')]: {
            display: 'block',
        },
    },
    search: {
        position: 'relative',
        borderRadius: theme.shape.borderRadius,
        backgroundColor: fade(theme.palette.common.white, 0.15),
        '&:hover': {
            backgroundColor: fade(theme.palette.common.white, 0.25),
        },
        marginRight: theme.spacing(2),
        marginLeft: 0,
        width: '100%',
        [theme.breakpoints.up('sm')]: {
            marginLeft: theme.spacing(3),
            width: 'auto',
        },
    },
    searchIcon: {
        width: theme.spacing(7),
        height: '100%',
        position: 'absolute',
        pointerEvents: 'none',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    inputRoot: {
        color: 'inherit',
    },
    inputInput: {
        padding: theme.spacing(1, 1, 1, 7),
        transition: theme.transitions.create('width'),
        width: '100%',
        [theme.breakpoints.up('md')]: {
            width: 200,
        },
    },
    sectionDesktop: {
        display: 'none',
        [theme.breakpoints.up('md')]: {
            display: 'flex',
        },
    },
    sectionMobile: {
        display: 'flex',
        [theme.breakpoints.up('md')]: {
            display: 'none',
        },
    },
}));

const MainBar = ({menuId, handleMenuClose, mobileMenuId, handleMobileMenuOpen}) => {
    const classes = useStyles();

    return <AppBar position="sticky">
        <Toolbar>
            <Typography className={classes.title} variant="h6" noWrap>
                Events
            </Typography>
            <div className={classes.search}>
                <AutosuggestionEvents formId='menu_'
                                      setResult={(event) => navigate('/events/' + event.id)}
                                      placeholder={'Search an event'}
                                      url={EVENT_SERVICE_URL + '/events'}
                                      urlParam={'name'}
                />
            </div>
            <div className={classes.grow}/>
            <div className={classes.sectionDesktop}>
                <ToolbarButtons menuId={menuId}
                                handleMenuClose={handleMenuClose}/>
            </div>
            <div className={classes.sectionMobile}>
                <IconButton aria-label="show more"
                            aria-controls={mobileMenuId}
                            aria-haspopup="true"
                            onClick={handleMobileMenuOpen}
                            color="inherit"
                >
                    <MoreIcon/>
                </IconButton>
            </div>
        </Toolbar>
    </AppBar>;
};

MainBar.propTypes = {
    userId: PropTypes.string,
    menuId: PropTypes.string.isRequired,
    handleMenuClose: PropTypes.func.isRequired,
    mobileMenuId: PropTypes.string.isRequired,
    handleMobileMenuOpen: PropTypes.func.isRequired
};

export default MainBar;