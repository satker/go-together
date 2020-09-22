import React from 'react';
import {makeStyles} from '@material-ui/core/styles';

import MobileMenu from "./Menu/Mobile";
import MainBar from "./MainBar";

const useStyles = makeStyles(theme => ({
    grow: {
        flexGrow: 1
    }
}));

const NavBar = () => {
    const classes = useStyles();
    const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = React.useState(null);

    const isMobileMenuOpen = Boolean(mobileMoreAnchorEl);

    const handleMenuClose = () => {
        handleMobileMenuClose();
    };


    const handleMobileMenuClose = () => {
        setMobileMoreAnchorEl(null);
    };

    const handleMobileMenuOpen = event => {
        setMobileMoreAnchorEl(event.currentTarget);
    };

    const menuId = 'primary-search-account-menu';

    const mobileMenuId = 'primary-search-account-menu-mobile';

    return (
        <div className={classes.grow}>
            <MainBar menuId={menuId}
                     mobileMenuId={mobileMenuId}
                     handleMenuClose={handleMenuClose}
                     handleMobileMenuOpen={handleMobileMenuOpen}/>
            <MobileMenu handleMobileMenuClose={handleMobileMenuClose}
                        isMobileMenuOpen={isMobileMenuOpen}
                        handleMenuClose={handleMenuClose}
                        mobileMenuId={mobileMenuId}
                        mobileMoreAnchorEl={mobileMoreAnchorEl}/>
        </div>
    );
};

export default NavBar;