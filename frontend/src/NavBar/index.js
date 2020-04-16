import React from 'react';
import {fade, makeStyles} from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import Badge from '@material-ui/core/Badge';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';
import AccountCircle from '@material-ui/icons/AccountCircle';
import AddCircle from '@material-ui/icons/AddCircle';
import NotificationsIcon from '@material-ui/icons/Notifications';
import MoreIcon from '@material-ui/icons/MoreVert';
import {connect} from "../Context";
import FormReference from "../utils/components/FormReference";
import {fetchAndSetToken} from "../utils/api/request";
import FormLogin from "../Login";
import {navigate} from 'hookrouter';
import {CSRF_TOKEN, EVENT_SERVICE_URL, USER_ID} from "../utils/constants";
import AutosuggestionLocation from "../utils/components/Autosuggestion";
import {set as setCookie} from "js-cookie";
import {FORM_ID} from "./constants";
import {setUserIdAndFetchWithToken} from "./actions";

const useStyles = makeStyles(theme => ({
    grow: {
        flexGrow: 1,
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

const NavBar = ({userId, titleName, setUserIdAndFetchWithToken}) => {
    const classes = useStyles();
    const [anchorEl, setAnchorEl] = React.useState(null);
    const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = React.useState(null);

    const isMenuOpen = Boolean(anchorEl);
    const isMobileMenuOpen = Boolean(mobileMoreAnchorEl);

    const logout = () => {
        setCookie(CSRF_TOKEN, null);
        setCookie(USER_ID, null);
        setUserIdAndFetchWithToken(['userId', 'fetchWithToken'], [null, fetchAndSetToken(null)]);
    };

    const goToEventPage = (event) => navigate('/events/' + event.id);

    const handleProfileMenuOpen = event => {
        setAnchorEl(event.currentTarget);
    };

    const handleMobileMenuClose = () => {
        setMobileMoreAnchorEl(null);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
        handleMobileMenuClose();
    };

    const handleMobileMenuOpen = event => {
        setMobileMoreAnchorEl(event.currentTarget);
    };

    const menuId = 'primary-search-account-menu';
    const renderMenu = (
        <Menu
            anchorEl={anchorEl}
            anchorOrigin={{vertical: 'top', horizontal: 'right'}}
            id={menuId}
            keepMounted
            transformOrigin={{vertical: 'top', horizontal: 'right'}}
            open={isMenuOpen}
            onClose={handleMenuClose}
        >
            <MenuItem button={false}
                      component={menuId}
                      onClick={handleMenuClose}>
                <FormReference formRef='/home' description='Profile'/>
            </MenuItem>
            <MenuItem button={false}
                      component={menuId}
                      onClick={handleMenuClose}>My events
            </MenuItem>
            <MenuItem button={false}
                      component={menuId}
                      onClick={handleMenuClose}>
                <FormReference formRef='/orders' description='Orders'/>
            </MenuItem>
            <MenuItem button={false}
                      component={menuId}
                      onClick={() => {
                          logout();
                          handleMenuClose();
                      }}>
                Logout
            </MenuItem>
        </Menu>
    );

    const loginArea = (formId) => [
        <FormLogin formId={formId}/>,
        <MenuItem button={false}
                  component={menuId}
                  onClick={() => {
                      navigate('/register');
                      handleMenuClose();
                  }}>
            Sign up
        </MenuItem
        >];

    const mobileMenuId = 'primary-search-account-menu-mobile';
    const renderMobileMenu = [
        <Menu
            key={mobileMenuId + '_menu'}
            anchorEl={mobileMoreAnchorEl}
            anchorOrigin={{vertical: 'top', horizontal: 'right'}}
            id={mobileMenuId}
            keepMounted
            transformOrigin={{vertical: 'top', horizontal: 'right'}}
            open={isMobileMenuOpen}
            onClose={handleMobileMenuClose}
        >
            {userId ? [
                <MenuItem button={true}
                          key={mobileMenuId + '_create'}
                          component={mobileMenuId}
                          onClick={() => navigate('/create', true)}>
                    <IconButton href='#'
                                aria-label="show 4 new mails"
                                color="inherit">
                        <Badge badgeContent={3}
                               color="secondary">
                            <AddCircle/>
                        </Badge>
                    </IconButton>
                </MenuItem>,
                <MenuItem button={true}
                          key={mobileMenuId + '_notification'}
                          component={mobileMenuId}>
                    <IconButton href='#'
                                aria-label="show 11 new notifications"
                                color="inherit">
                        <Badge badgeContent={11}
                               color="secondary">
                            <NotificationsIcon/>
                        </Badge>
                    </IconButton>
                </MenuItem>,
                <MenuItem button={true}
                          key={mobileMenuId + '_account'}
                          component={mobileMenuId}
                          onClick={handleProfileMenuOpen}>
                    <IconButton href='#'
                                aria-label="account of current user"
                                aria-controls="primary-search-account-menu"
                                aria-haspopup="true"
                                color="inherit"
                    >
                        <AccountCircle/>
                    </IconButton>
                </MenuItem>
            ] : loginArea(mobileMenuId)}
        </Menu>
    ];

    return (
        <div className={classes.grow}>
            <AppBar position="sticky">
                <Toolbar>
                    <Typography className={classes.title} variant="h6" noWrap>
                        {titleName}
                    </Typography>
                    <div className={classes.search}>
                        <AutosuggestionLocation formId='menu_'
                                                setResult={goToEventPage}
                                                placeholder={'Search an event'}
                                                url={EVENT_SERVICE_URL + '/events'}
                                                urlParam={'name'}
                        />
                    </div>
                    <div className={classes.grow}/>
                    <div className={classes.sectionDesktop}>
                        {userId ? [<IconButton onClick={() => navigate('/create', true)}
                                               key={menuId + '_create'}
                                               aria-label="show 4 new mails"
                                               color="inherit">
                                <Badge color="secondary">
                                    <AddCircle/>
                                </Badge>
                            </IconButton>,
                                <IconButton href='/#'
                                            key={menuId + '_notification'}
                                            aria-label="show 17 new notifications"
                                            color="inherit">
                                    <Badge badgeContent={17}
                                           color="secondary">
                                        <NotificationsIcon/>
                                    </Badge>
                                </IconButton>,
                                <IconButton
                                    key={menuId + '_account'}
                                    edge="end"
                                    aria-label="account of current user"
                                    aria-controls={menuId}
                                    aria-haspopup="true"
                                    onClick={handleProfileMenuOpen}
                                    color="inherit"
                                >
                                    <AccountCircle/>
                                </IconButton>] :
                            loginArea(menuId)
                        }
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
            </AppBar>
            {renderMobileMenu}
            {renderMenu}
        </div>
    );
};

const mapStateToProps = (state) => ({
    userId: state.userId,
    titleName: state.titleName
});

export default connect(mapStateToProps, {setUserIdAndFetchWithToken}, FORM_ID)(NavBar);