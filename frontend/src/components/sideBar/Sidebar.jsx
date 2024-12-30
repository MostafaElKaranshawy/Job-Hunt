import { Link, useParams } from 'react-router-dom';
import { Nav, NavItem } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './Sidebar.css';

function Sidebar() {
  const { companyUsername } = useParams();

  return (
    <div className="text-white sidebar">
      <div className="text-center py-4">
        <img
          src="/new-logo.png"
          alt="JobHunt Logo"
          className="sidebar-logo"
        />
      </div>
      <Nav className="flex-column px-3">
        <NavItem className="navItem">
          <Link to={`/company/${companyUsername}`} className="text-decoration-none py-2 d-block company-link">
            <i className="bi bi-person me-2"></i>
            Profile
          </Link>
        </NavItem>
        <NavItem className="navItem">
          <Link to={`/company/${companyUsername}/jobs`} className="text-decoration-none py-2 d-block company-link">
            <i className="bi bi-briefcase me-2"></i>
            Jobs
          </Link>
        </NavItem>
      <div
        className="navItem logout py-2 d-block company-link"
        onClick={() => {
          console.log("Logout");
          document.cookie = ";";
          window.location.href = "/";
        }}
      >
        <i className="bi bi-box-arrow-right me-2"></i>
          Log out
    </div>
</Nav>

    </div>
  );
};

export default Sidebar;
