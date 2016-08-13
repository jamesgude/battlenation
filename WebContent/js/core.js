var app = {
		loadPosts: function(selector) {
			$.ajax({
				url: 'post.html',
				success: function(response) {
					$(selector).append(response);
					console.log(response);
				}
			})			
		}
		
}
$(document).ready(function() {
	$(".loadMorePosts").on("click", function(e) {
		e.preventDefault();
		app.loadPosts('#featured');
		return false;
	});
});