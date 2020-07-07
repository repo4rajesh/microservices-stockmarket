package com.bonasys.stock.dbservice.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonasys.stock.dbservice.model.Quote;
import com.bonasys.stock.dbservice.model.Quotes;
import com.bonasys.stock.dbservice.repository.QuotesRepository;

@RestController
@RequestMapping("/rest/db")
public class DBServiceResource {
	
	@Autowired
	private QuotesRepository quotesRepository;

	@GetMapping("/{username}")
	public List<String> getQuotes(@PathVariable("username") 
									final String username){
		return getQuotesByUserName(username);
	}

	private List<String> getQuotesByUserName(final String username) {
		return quotesRepository.findByUserName(username)
						.stream()
						.map(Quote::getQuote)
						.collect(Collectors.toList());
	}
	
	@PostMapping("/add")
	public List<String> add(@RequestBody final Quotes quotes){
		quotes.getQuotes()
			   .stream()
			   .map(quote -> new Quote(quotes.getUserName(), quote))
			   .forEach(quote -> quotesRepository.save(quote));
		return getQuotesByUserName(quotes.getUserName());
	}
	
	@PostMapping("/delete/{username}")
	public List<String> deleteByUserName(@PathVariable("username") final String username){
		
		List<Quote> quotes = quotesRepository.findByUserName(username);
		quotes.stream()
				.forEach(quote -> quotesRepository.delete(quote));
		return getQuotesByUserName(username);
	}
	
}
