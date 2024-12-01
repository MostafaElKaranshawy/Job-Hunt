import { Link } from 'react-router-dom';
import { Nav, NavItem } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './Sidebar.css';

function Sidebar() {
  return (
    <div className="text-white sidebar">
      <h3 className="text-white text-center py-4">JobHunt</h3>
      <Nav className="flex-column px-3">
        <NavItem className="navItem">
          <Link to="/" className="text-decoration-none py-2 d-block link">
            <i className="bi bi-person me-2"></i>
            Profile
          </Link>
        </NavItem>
        <NavItem className="navItem">
          <Link to="/jobs" className="text-decoration-none py-2 d-block link">
            <i className="bi bi-briefcase me-2"></i>
            Jobs
          </Link>
        </NavItem>
      </Nav>
    </div>
  );
};

export default Sidebar;
