import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const [bookmarks, setBookmarks] = useState([]);
  const [search, setSearch] = useState("");
  const navigate = useNavigate();
  const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

  const fetchBookmarks = async () => {
    const token = localStorage.getItem("token");
    const response = await fetch(`${BACKEND_URL}/api/bookmarks/viewBookmarks`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`
      },
    });
    if (response.ok) {
      const data = await response.json();
      setBookmarks(data.bookmarks);
    } else if (response.status === 403 || response.status === 401) {
      handleLogout();
    }
  };

  useEffect(() => {
    fetchBookmarks();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  const uploadBookmarks = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const token = localStorage.getItem("token");
    const formData = new FormData();
    formData.append("file", file);
    const response = await fetch(`${BACKEND_URL}/api/bookmarks/upload`, {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },  
      body: formData
    });
    if (response.ok) {
      fetchBookmarks();
    } else {
      alert("Upload failed!");
    }
  };

  const downloadBookmarks = async () => {
    const token = localStorage.getItem("token");
    const response = await fetch(`${BACKEND_URL}/api/bookmarks/download`, {
      method: "GET",
      headers: { Authorization: `Bearer ${token}`}
    });
    if (!response.ok) {
      alert("Download failed!");
    }
    const res = await response.text();
    const blob = new Blob([res], { type: "text/html" });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "bookmarks.html";
    link.click();
  };

  const filteredBookmarks = bookmarks.filter(b => 
    (b.title?.toLowerCase() ?? "").includes(search.toLowerCase()) || 
    (b.url?.toLowerCase() ?? "").includes(search.toLowerCase())
  );

  const deleteBookmark = async (bId) => {
    const token = localStorage.getItem("token");
    const response = await fetch(`${BACKEND_URL}/api/bookmarks/delete/${bId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`
      },
    });
    if (response.ok) {
      fetchBookmarks();
    } else if (response.status === 403 || response.status === 401) {
      handleLogout();
    }
  };

  const deleteAllBookmarks = async () => {
    const token = localStorage.getItem("token");
    const response = await fetch(`${BACKEND_URL}/api/bookmarks/deleteAll`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`
      },
    });
    if (response.ok) {
      fetchBookmarks();
    } else if (response.status === 403 || response.status === 401) {
      handleLogout();
    }
  };

  const deleteAccount = async () => {
    const token = localStorage.getItem("token");
    const response = await fetch(`${BACKEND_URL}/api/users/delete`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`
      },
    });
    if (response.ok) {
      handleLogout();
    } else if (response.status === 403 || response.status === 401) {
      handleLogout();
    }
  };

  return (
    <div className="min-h-screen bg-base-200 flex flex-col">
      <div className="navbar bg-base-100 shadow-sm px-8">
        <div className="flex-1">
          <span className="text-xl font-bold text-primary">BookmarkVault</span>
        </div>
        <div className="flex-none gap-2">
          <input 
            type="file" 
            id="fileInput" 
            className="hidden" 
            onChange={uploadBookmarks} 
          />
          <button onClick={() => {document.getElementById('fileInput').click()}} className="btn btn-ghost btn-sm">Import Bookmarks</button>
        </div>
        <div className="flex-none gap-2">
          <button onClick={downloadBookmarks} className="btn btn-ghost btn-sm">Download Bookmarks</button>
        </div>
        <div className="flex-none gap-2">
          <button onClick={deleteAllBookmarks} className="btn btn-ghost btn-sm">Delete All Bookmarks</button>
        </div>
        <div className="flex-none gap-2">
          <button onClick={deleteAccount} className="btn btn-ghost btn-sm">Delete Account</button>
        </div>
        <div className="flex-none gap-2">
          <button onClick={handleLogout} className="btn btn-ghost btn-sm">Logout</button>
        </div>
      </div>

      <main className="p-8 flex-grow">
        <div className="max-w-6xl mx-auto space-y-6">
          <div className="flex flex-col md:flex-row gap-4 justify-between items-end">
            <div className="stats shadow bg-base-100 w-full md:w-auto">
              <div className="stat">
                <div className="stat-title">Total Bookmarks</div>
                <div className="stat-value text-primary">{bookmarks.length}</div>
              </div>
            </div>
            
            <div className="form-control w-full max-w-xs">
              <input 
                type="text" 
                placeholder="Search bookmarks..." 
                className="input input-bordered" 
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
          </div>

          <div className="card bg-base-100 shadow-xl">
            <div className="card-body p-0">
              <div className="overflow-x-auto">
                <table className="table table-zebra w-full">
                  <thead>
                    <tr className="bg-base-200">
                      <th>Name</th>
                      <th>URL</th>
                      <th className="text-right">Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredBookmarks.length > 0 ? (
                      filteredBookmarks.map((b) => (
                        <tr key={b.bookmarkId} className="hover">
                          <td className="font-semibold text-base-content">{b.bookmarkName || "Untitled"}</td>
                          <td>
                            <a href={b.bookmarkLink} target="_blank" rel="noreferrer" className="link link-primary opacity-70 hover:opacity-100">
                              {b.bookmarkLink}
                            </a>
                          </td>
                          <td className="text-right">
                            {/* <button className="btn btn-square btn-ghost btn-sm text-blue-500 m-1">
                                Edit
                            </button> */}
                            <button className="btn btn-square btn-ghost btn-sm text-error m-1"
                              onClick={() => deleteBookmark(b.bookmarkId)}
                            >
                              Delete
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="3" className="text-center py-10 text-base-content/50">No bookmarks found.</td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;