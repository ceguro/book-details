# book-details
This is an test about GraphQL

Example with PlayGround

# Write your query or mutation here
{
  bookById(id: "book-1"){
    id
    name
    pageCount
    author{
      firstName
      lastName
    }
  }
}
