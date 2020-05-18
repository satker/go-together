import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {connect} from "../../App/Context";
import {cleanToken, cleanUserId} from "./actions";
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

const mapStateToProps = () => (state) => ({
    userId: state.userId.value
});

export default connect(mapStateToProps, {cleanUserId, cleanToken})(NavBar);